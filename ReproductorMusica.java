import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;


public class ReproductorMusica extends JFrame implements ActionListener, ChangeListener {
    private JButton playButton, stopButton, prevButton, nextButton;
    private JLabel timeLabel, coverLabel;
    private JSlider timeSlider;
    private Clip clip;
    private boolean isPlaying;
    private ArrayList<Cancion> listaCanciones;
    private int currentIndex;

    public ReproductorMusica(ArrayList<Cancion> listaCanciones) {
        this.listaCanciones = listaCanciones;
        this.currentIndex = 0;

        setTitle("Reproductor de Música");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel para la imagen de la portada
        JPanel coverPanel = new JPanel(new GridBagLayout());
        coverLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource(listaCanciones.get(currentIndex).getPortada()));
        coverLabel.setIcon(icon);
        coverPanel.add(coverLabel, new GridBagConstraints());
        add(coverPanel, BorderLayout.CENTER);

        // Panel de control
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        playButton = new JButton("Play");
        playButton.addActionListener(this);
        controlPanel.add(playButton, gbc);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        controlPanel.add(stopButton, gbc);

        prevButton = new JButton("Prev");
        prevButton.addActionListener(this);
        controlPanel.add(prevButton, gbc);

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        controlPanel.add(nextButton, gbc);

        add(controlPanel, BorderLayout.SOUTH);

        // Etiqueta de tiempo
        timeLabel = new JLabel("Tiempo: 0:00", JLabel.CENTER);
        add(timeLabel, BorderLayout.NORTH);

        // Barra de reproducción
        timeSlider = new JSlider(0, 100, 0);
        timeSlider.setEnabled(false);
        timeSlider.addChangeListener(this);
        add(timeSlider, BorderLayout.WEST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            if (!isPlaying) {
                reproducirCancion();
                isPlaying = true;
            }
            
        } else if (e.getSource() == stopButton) {
            detenerCancion();
        } else if (e.getSource() == prevButton) {
            reproducirCancionAnterior();
        } else if (e.getSource() == nextButton) {
            reproducirSiguienteCancion();
        }
    }

    private void reproducirCancion() {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(listaCanciones.get(currentIndex).getRuta())));
            clip.start();

            new Thread(() -> {
                while (clip.isActive()) {
                    long microSeconds = clip.getMicrosecondPosition();
                    long duration = clip.getMicrosecondLength();
                    int value = (int) (100 * microSeconds / duration);
                    SwingUtilities.invokeLater(() -> {
                        timeSlider.setValue(value);
                        actualizarTiempo();
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                isPlaying = false;
            }).start();

            timeSlider.setEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void detenerCancion() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isPlaying = false;
        }
    }

    private void reproducirCancionAnterior() {
        detenerCancion();
        currentIndex = (currentIndex - 1 + listaCanciones.size()) % listaCanciones.size();
        actualizarPortada();
        reproducirCancion();
    }
    
    private void reproducirSiguienteCancion() {
        detenerCancion();
        currentIndex = (currentIndex + 1) % listaCanciones.size();
        actualizarPortada();
        reproducirCancion();
    }
    

    private void actualizarPortada() {
        ImageIcon icon = new ImageIcon(getClass().getResource(listaCanciones.get(currentIndex).getPortada()));
        coverLabel.setIcon(icon);
    }

    private void actualizarTiempo() {
        long microSeconds = clip.getMicrosecondPosition();
        long seconds = microSeconds / 1_000_000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        timeLabel.setText("Tiempo: " + minutes + ":" + String.format("%02d", seconds));
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == timeSlider && clip != null) {
            if (!timeSlider.getValueIsAdjusting()) {
                long duration = clip.getMicrosecondLength();
                long newPosition = (long) (duration * timeSlider.getValue() / 100.0);
                clip.setMicrosecondPosition(newPosition);
            }
        }
    }
}